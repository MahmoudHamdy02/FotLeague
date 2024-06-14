import { pool } from "../db";
import { League } from "../types/League";
import { LeagueUser } from "../types/LeagueUser";

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

export const addLeagueUser = async (userId: number, leagueId: number): Promise<LeagueUser> => {
    const data = await pool.query<LeagueUser>("INSERT INTO leagues_users(user_id, league_id) VALUES ($1, $2) RETURNING *;", [userId, leagueId]);
    return data.rows[0];
};

// TODO: Make these two consistent
export const getLeagueUsers = async (leagueId: number): Promise<number[]> => {
    const users = await pool.query<LeagueUser>("SELECT user_id FROM leagues_users WHERE league_id = $1", [leagueId]);
    return users.rows.map(user => user.user_id);
};

export const getUserLeagues = async (userId: number): Promise<League[]> => {
    const leagues = await pool.query<League>("SELECT L.* FROM leagues_users AS LU JOIN leagues AS L ON LU.league_id = L.id WHERE LU.user_id = $1", [userId]);
    return leagues.rows;
};

export * as leagueService from "./league.service";