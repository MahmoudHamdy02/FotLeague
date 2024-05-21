import { Strategy as LocalStrategy } from "passport-local";
import bcrypt from "bcryptjs";
import passport from "passport";
import { pool } from "../db";
import { User } from "../types/User";
import { QueryResult } from "pg";

type _User = User;

declare global {
    // eslint-disable-next-line @typescript-eslint/no-namespace
    namespace Express {
        interface User extends _User {}
    }
}

passport.serializeUser((user: User, done) => {
    done(null, user.id);
});

passport.deserializeUser(async (userId: number, done) => {
    const data = await pool.query("SELECT * FROM users WHERE id = $1", [userId]);
    return done(null, data.rows[0] as User);
});

export default passport.use(new LocalStrategy({usernameField: "email"}, async function verify(username, password, cb) {
    pool.query("SELECT * FROM users WHERE email = $1", [ username ], async function(err, row: QueryResult<User>) {
        if (err)
            return cb(err);

        if (row.rowCount == 0)
            return cb(null, false, { message: "Incorrect email or password." });

        const passwordsMatch = await bcrypt.compare(
            password,
            row.rows[0].password
        );

        if (passwordsMatch) {
            return cb(null, row.rows[0]);
        } else {
            return cb(null, false, { message: "Incorrect email or password." });
        }
    });
}));