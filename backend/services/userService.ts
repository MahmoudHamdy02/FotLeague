import { pool } from "../db";
import { User } from "../types/User";
import bcrypt from "bcryptjs";

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

export const createUser = async (email: string, password: string, name: string, role: number): Promise<User> => {
    const client = await pool.connect();

    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(password, salt);

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
        console.log(error);
        throw error;
    } finally {
        client.release();
    }
};
