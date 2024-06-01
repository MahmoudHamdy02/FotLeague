import { pool } from "../db";
import { Score } from "../types/Score";

export const getUserScoresBySeason = async (userId: number, season: number): Promise<Score[]> => {
    const data = await pool.query<Score>("SELECT S.* FROM scores S JOIN matches M ON S.match_id = M.id WHERE S.user_id = $1 AND M.season = $2", [userId, season]);
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

export * as scoreService from "./scoreService";