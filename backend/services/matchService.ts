import { pool } from "../db";
import { Match } from "../types/Match";


export const insertMatches = async (matches: Omit<Match, "id">[]): Promise<Match[]> => {
    try {
        const data = await pool.query<Match>(
            `INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek)
            SELECT home, away, home_score, away_score, match_status, datetime, season, gameweek 
            FROM jsonb_to_recordset($1::jsonb) 
            AS t (home text, away text, home_score int, away_score int, match_status int, datetime timestamp, season text, gameweek int)
            RETURNING *
            `,
            [JSON.stringify(matches)]
        );
        return data.rows;
    } catch(error) {
        console.log(error);
        throw error;
    }
};