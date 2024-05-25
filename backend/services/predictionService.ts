import { pool } from "../db";
import { Prediction } from "../types/Prediction";

export const getUserPredictionsBySeason = async (user_id: number, season: string): Promise<Prediction[]> => {
    try {
        const data = await pool.query<Prediction>("SELECT p.id, p.user_id, p.match_id, p.home, p.away FROM predictions AS p JOIN matches AS m ON p.match_id = m.id WHERE m.season=$1 AND p.user_id=$2;", [season, user_id]);
        return data.rows;
    } catch (error) {
        console.log(error);
        throw error;
    }
};

export const addPrediction = async (prediction: Omit<Prediction, "id">): Promise<Prediction> => {
    try {
        const data = await pool.query<Prediction>("INSERT INTO predictions(user_id, match_id, home, away) VALUES ($1, $2, $3, $4) RETURNING *", [
            prediction.user_id,
            prediction.match_id,
            prediction.home,
            prediction.away
        ]);
        return data.rows[0];
    } catch (error) {
        console.log(error);
        throw error;
    }
};

export const editPredictionScores = async (id: number, home: number, away: number): Promise<Prediction> => {
    try {
        const data = await pool.query("UPDATE predictions SET home = $1, away = $2 WHERE id = $3 RETURNING *", [home, away, id]);
        return data.rows[0];
    } catch (error) {
        console.log(error);
        throw error;
    }
};