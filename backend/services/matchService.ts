import { pool } from "../db";
import { MatchStatus } from "../enums/MatchStatus";
import { Match } from "../types/Match";


export const getMatchDetails = async (matchId: number): Promise<Match> => {
    const data = await pool.query<Match>("SELECT * FROM matches WHERE id = $1", [matchId]);
    return data.rows[0];
};

export const updateMatchStatus = async (matchId: number, status: MatchStatus): Promise<Match> => {
    const data = await pool.query<Match>("UPDATE matches SET match_status = $1 WHERE id = $2 RETURNING *;", [status, matchId]);
    return data.rows[0];
};

export const getCurrentSeason = async (): Promise<number> => {
    const data = await pool.query("SELECT MAX(season) FROM matches;");
    return data.rows[0].max;
};

export const getMatchesBySeason = async (season: string): Promise<Match[]> => {
    const data = await pool.query<Match>("SELECT * FROM matches WHERE season = $1;", [season]);
    return data.rows;
};

export const insertMatches = async (matches: Omit<Match, "id">[]): Promise<Match[]> => {
    const data = await pool.query<Match>(
        `INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek)
        SELECT home, away, home_score, away_score, match_status, datetime, season, gameweek 
        FROM jsonb_to_recordset($1::jsonb) 
        AS t (home text, away text, home_score int, away_score int, match_status int, datetime timestamp, season int, gameweek int)
        RETURNING *
        `,
        [JSON.stringify(matches)]
    );
    return data.rows;
};

export * as matchService from "./matchService";