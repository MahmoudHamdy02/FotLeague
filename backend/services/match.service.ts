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

export const updateMatch = async (matchId: number, status: MatchStatus, homeScore: number, awayScore: number, datetime: string, liveTime: string | null): Promise<Match> => {
    const data = await pool.query<Match>("UPDATE matches SET home_score = $1, away_score = $2, match_status = $3, datetime = $4, live_time = $5 WHERE id = $6 RETURNING *;", [
        homeScore,
        awayScore,
        status,
        datetime,
        liveTime,
        matchId
    ]);
    return data.rows[0];
};

export const getCurrentSeason = async (): Promise<number> => {
    const data = await pool.query("SELECT MAX(season) FROM matches;");
    return data.rows[0].max;
};

export const getMatchesBySeason = async (season: number): Promise<Match[]> => {
    const data = await pool.query<Match>("SELECT * FROM matches WHERE season = $1 ORDER BY datetime;", [season]);
    return data.rows;
};

export const insertMatches = async (matches: Omit<Match, "id">[]): Promise<Match[]> => {
    const data = await pool.query<Match>(
        `INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek, live_time)
        SELECT home, away, home_score, away_score, match_status, datetime, season, gameweek, live_time 
        FROM jsonb_to_recordset($1::jsonb) 
        AS t (home text, away text, home_score int, away_score int, match_status int, datetime timestamp, season int, gameweek int, live_time text)
        RETURNING *
        `,
        [JSON.stringify(matches)]
    );
    return data.rows;
};

export * as matchService from "./match.service";