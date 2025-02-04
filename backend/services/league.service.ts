import { pool } from "../db";
import { League } from "../types/League";
import { LeagueCode } from "../types/LeagueCode";
import { LeagueDto } from "../types/LeagueDto";
import { LeagueUser } from "../types/LeagueUser";
import { UserScore } from "../types/UserScore";

export const createLeagueCode = async (code: string): Promise<LeagueCode> => {
    const data = await pool.query<LeagueCode>("INSERT INTO league_codes(code) VALUES ($1) RETURNING *;", [code]);
    return data.rows[0];
};

export const createLeague = async (name: string, ownerId: number, codeId: number): Promise<League> => {
    const client = await pool.connect();
    try {
        await client.query("BEGIN");
        // Create new league
        const league = await pool.query<League>("INSERT INTO leagues(name, owner_id, code_id) VALUES ($1, $2, $3) RETURNING *;", [name, ownerId, codeId]);
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

export const getLeagueCodeById = async (id: number): Promise<LeagueCode | null> => {
    const data = await pool.query<LeagueCode>("SELECT * FROM league_codes WHERE id = $1 LIMIT 1;", [id]);
    return data.rowCount === 1
        ? data.rows[0]
        : null;
};

export const getLeagueCodeByCode = async (code: string): Promise<LeagueCode | null> => {
    const data = await pool.query<LeagueCode>("SELECT * FROM league_codes WHERE code = $1 LIMIT 1;", [code]);
    return data.rowCount === 1
        ? data.rows[0]
        : null;
};

export const getLeagueByCode = async (code: string): Promise<League | null> => {
    const data = await pool.query<League>("SELECT leagues.* FROM leagues JOIN league_codes ON leagues.code_id = league_codes.id WHERE league_codes.code = $1 LIMIT 1;", [code]);
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

export const getUserLeagues = async (userId: number): Promise<LeagueDto[]> => {
    const leagues = await pool.query<LeagueDto>("SELECT L.id, L.name, L.owner_id, LC.code FROM leagues_users AS LU JOIN leagues AS L ON LU.league_id = L.id JOIN league_codes AS LC ON L.code_id = LC.id WHERE LU.user_id = $1", [userId]);
    return leagues.rows;
};

export const renameLeague = async (leagueId: number, name: string): Promise<LeagueDto> => {
    const league = await pool.query<LeagueDto>("WITH updated_league AS (UPDATE leagues SET name = $1 WHERE id = $2 RETURNING *) SELECT L.id, L.name, L.owner_id, LC.code FROM updated_league AS L JOIN league_codes AS LC ON L.code_id = LC.id;", [name, leagueId]);
    return league.rows[0];
};

export const updateLeagueCode = async (leagueId: number, codeId: number): Promise<League> => {
    const league = await pool.query<League>("UPDATE leagues SET code_id = $1 WHERE id = $2 RETURNING *;", [codeId, leagueId]);
    return league.rows[0];
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