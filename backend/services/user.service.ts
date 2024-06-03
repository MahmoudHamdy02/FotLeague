import { pool } from "../db";
import { User } from "../types/User";
import { hashPassword } from "../utils";

export const getUserById = async (id: number): Promise<User> => {
    const data = await pool.query<User>("SELECT * FROM users WHERE id = $1;", [id]);
    return data.rows[0];
};

export const getUserByEmail = async (email: string): Promise<User> => {
    const data = await pool.query<User>("SELECT * FROM users WHERE email = $1;", [email]);
    return data.rows[0];
};

export const createUser = async (email: string, password: string, name: string, role: number): Promise<User> => {
    const client = await pool.connect();

    const hashedPassword = await hashPassword(password);

    try {
        await client.query("BEGIN");
        // Create user record
        const data = await client.query<User>("INSERT INTO users(email, password, name, role) VALUES ($1, $2, $3, $4) RETURNING *;", [
            email,
            hashedPassword,
            name,
            role
        ]);
            // Automatically include user in global league
        await client.query("INSERT INTO leagues_users(user_id, league_id) VALUES ($1, 1);", [data.rows[0].id]);
        await client.query("COMMIT");
        return data.rows[0];
    } catch (error) {
        await client.query("ROLLBACK");
        throw error;
    } finally {
        client.release();
    }
};

export const changeUserPassword = async (userId: number, password: string): Promise<User> => {
    const hashedPassword = await hashPassword(password);

    const data = await pool.query<User>("UPDATE users SET password = $1 WHERE id = $2 RETURNING *;", [hashedPassword, userId]);
    return data.rows[0];
};

export * as userService from "./user.service";