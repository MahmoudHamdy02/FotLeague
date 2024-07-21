import { pool } from "../db";
import { League } from "../types/League";
import { LeagueUser } from "../types/LeagueUser";
import { UserScore } from "../types/UserScore";

export const createLeague = async (name: string, ownerId: number, code: string): Promise<League> => {
    const client = await pool.connect();
    try {
        await client.query("BEGIN");
        // Create new league
        const league = await pool.query<League>("INSERT INTO leagues(name, owner_id, code) VALUES ($1, $2, $3) RETURNING *;", [name, ownerId, code]);
        // Automatically insert user into their league
        await pool.query("INSERT INTO leagues_users(league_id, user_id) VALUES ($1, $2);", [league.rows[0].id, ownerId]);

        await pool.query("COMMIT");
        return league.rows[0];
    } catch (error) {
        await pool.query("ROLLBACK");
        throw error;
    } finally {
        client.release();
    }
};

export const getLeagueByCode = async (code: string): Promise<League | null> => {
    const data = await pool.query<League>("SELECT * FROM leagues WHERE code = $1 LIMIT 1;", [code]);
    return data.rowCount === 1
        ? data.rows[0]
        : null;
};

export const getLeagueById = async (id: number): Promise<League | null> => {
    const data = await pool.query<League>("SELECT * FROM leagues WHERE id = $1;", [id]);
    return data.rowCount === 1
        ? data.rows[0]
        : null;
};

export const addLeagueUser = async (userId: number, leagueId: number): Promise<LeagueUser> => {
    const data = await pool.query<LeagueUser>("INSERT INTO leagues_users(user_id, league_id) VALUES ($1, $2) RETURNING *;", [userId, leagueId]);
    return data.rows[0];
};

export const getLeagueUsers = async (leagueId: number): Promise<number[]> => {
    const users = await pool.query<LeagueUser>("SELECT user_id FROM leagues_users WHERE league_id = $1", [leagueId]);
    return users.rows.map(user => user.user_id);
};

export const getLeagueUsersWithScores = async (leagueId: number): Promise<UserScore[]> => {
    const users = await pool.query<UserScore>("SELECT U.id, U.name, COALESCE(SUM(S.score), 0) AS score FROM leagues_users AS LU JOIN users AS U ON LU.user_id = U.id LEFT JOIN scores AS S ON S.user_id = U.id WHERE LU.league_id = $1 GROUP BY U.id ORDER BY score DESC;", [leagueId]);
    return users.rows;
};

export const getUserLeagues = async (userId: number): Promise<League[]> => {
    const leagues = await pool.query<League>("SELECT L.* FROM leagues_users AS LU JOIN leagues AS L ON LU.league_id = L.id WHERE LU.user_id = $1", [userId]);
    return leagues.rows;
};

export const deleteLeagueUser = async (userId: number, leagueId: number): Promise<LeagueUser> => {
    const data = await pool.query<LeagueUser>("DELETE FROM leagues_users WHERE user_id = $1 AND league_id = $2 RETURNING *;", [userId, leagueId]);
    return data.rows[0];
};

export const deleteLeague = async (leagueId: number): Promise<League> => {
    const league = await pool.query<League>("DELETE FROM leagues WHERE id = $1 RETURNING *;", [leagueId]);
    return league.rows[0];
};

export * as leagueService from "./league.service";