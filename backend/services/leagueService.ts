import { pool } from "../db";
import { League } from "../types/League";

namespace leagueService {
    // Todo: Automatically insert user into leagues_users
    export const createLeague = async (name: string, owner: number): Promise<League> => {
        try {
            const data = await pool.query("INSERT INTO leagues(name, owner) VALUES ($1, $2) RETURNING *;", [name, owner]);
            return data.rows[0] as League;
        } catch (error) {
            console.log(error);
            throw error;
        }
    };
}

export default leagueService;