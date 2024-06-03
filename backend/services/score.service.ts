import { pool } from "../db";
import { Score } from "../types/Score";

export const getTotalUserScoreBySeason = async (userId: number, season: number): Promise<number> => {
    const data = await pool.query("SELECT SUM(S.score) FROM scores AS S JOIN matches AS M ON S.match_id = M.id WHERE S.user_id = $1 AND M.season = $2", [userId, season]);
    return parseInt(data.rows[0].sum);
};

export const getUserScoresBySeason = async (userId: number, season: number): Promise<Score[]> => {
    const data = await pool.query<Score>("SELECT S.* FROM scores S JOIN matches M ON S.match_id = M.id WHERE S.user_id = $1 AND M.season = $2", [userId, season]);
    return data.rows;
};

export const getTopGlobalUsersBySeason = async (num: number, season: number): Promise<{name: string, score: number}[]> => {
    const data = await pool.query<{name: string, score: number}>(
        `
        SELECT U.name, SUM(S.score) AS total_score
        FROM users AS U 
        JOIN scores AS S ON U.id = S.user_id 
        JOIN matches AS M ON S.match_id = M.id
        WHERE M.season = $1
        GROUP BY U.name
        ORDER BY total_score DESC
        LIMIT $2;
    `, [season, num]
    );
    return data.rows;
};

export const addScores = async (scores: Score[]): Promise<void> => {
    await pool.query(
        `INSERT INTO scores (user_id, match_id, score)
        SELECT user_id, match_id, score 
        FROM jsonb_to_recordset($1::jsonb) 
        AS t (user_id int, match_id int, score int);
        `,
        [JSON.stringify(scores)]
    );
};

export * as scoreService from "./score.service";