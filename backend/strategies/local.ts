import { Strategy as LocalStrategy } from "passport-local";
import passport from "passport";
import bcrypt from "bcryptjs";
import { User } from "../types/User";
import userService from "../services/userService";

type _User = User;
declare global {
    namespace Express {
        interface User extends _User {}
    }
}

passport.serializeUser((user: User, done) => {
    done(null, user.id);
});

passport.deserializeUser(async (userId: number, done) => {
    try {
        const user = await userService.getUserById(userId);
        return done(null, user);
    } catch (e) {
        return done(e, null);
    }
});

export default passport.use(new LocalStrategy({usernameField: "email"}, async function verify(email, password, cb) {
    try {
        const user = await userService.getUserByEmail(email);

        if (!user)
            return cb(null, false, { message: "Incorrect email or password." });

        const passwordsMatch = await bcrypt.compare(
            password,
            user.password
        );

        if (passwordsMatch) {
            return cb(null, user);
        } else {
            return cb(null, false, { message: "Incorrect email or password." });
        }
    } catch (e) {
        return cb(e);
    }
}));