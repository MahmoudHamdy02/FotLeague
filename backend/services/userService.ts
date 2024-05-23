import { pool } from "../db";
import { User } from "../types/User";
import bcrypt from "bcryptjs";

namespace userService {
    export const getUserById = async (id: number): Promise<User> => {
        try {
            const data = await pool.query("SELECT * FROM users WHERE id = $1", [id]);
            return data.rows[0] as User;
        } catch (error) {
            console.log(error);
            throw error;
        }
    };

    export const getUserByEmail = async (email: string): Promise<User> => {
        try {
            const data = await pool.query("SELECT * FROM users WHERE email = $1", [email]);
            return data.rows[0] as User;
        } catch (error) {
            console.log(error);
            throw error;
        }
    };

    export const createUser = async (email: string, password: string, name: string): Promise<void> => {
        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(password, salt);
        try {
            await pool.query("INSERT INTO users(email, password, name) VALUES ($1, $2, $3);", [email, hashedPassword, name]);
        } catch (error) {
            console.log(error);
            throw error;
        }
    };
}

export default userService;