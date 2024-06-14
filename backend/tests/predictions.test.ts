import {describe, it, expect, afterAll, beforeAll} from "@jest/globals";
import app from "../app";
import request from "supertest";
import { pool } from "../db";
import { resetDB } from "./utils";


beforeAll(async () => {
    await resetDB();
});

afterAll(async () => {
    await pool.end();
});

describe("Prediction System", () => {
    let cookie: string;

    const newUser = {
        email: "test@gmail.com",
        password: "test123",
        name: "testuser"
    };
    let user_id: number;

    beforeAll(async () => {
        const res = await request(app).post("/auth/signup")
                .send(newUser);
        user_id = res.body.id;
        cookie = res.headers["set-cookie"][0];
        await pool.query("INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek) VALUES ('Liverpool', 'Everton', NULL, NULL, 1, '2024-12-14 18:00:00', 2024, 1);");
    });

    it("returns empty prediction list for new users", async () => {
        const res = await request(app).get("/predictions/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(res.body.length).toEqual(0);
        expect(res.body).toEqual([]);
    });

    it("submits a user prediction for a match", async () => {
        const res = await request(app).post("/predictions/")
                .send({
                    matchId: 1,
                    home: 3,
                    away: 1
                })
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(201);
        expect(res.body.user_id).toBe(user_id);
        expect(res.body.match_id).toBe(1);
        expect(res.body.home).toBe(3);
        expect(res.body.away).toBe(1);
    });

    it("returns prediction list with one prediction", async () => {
        const res = await request(app).get("/predictions/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(res.body.length).toEqual(1);
        expect(res.body).toEqual([
            {
                user_id: user_id,
                match_id: 1,
                home: 3,
                away: 1
            }
        ]);
    });

    it("throws an error when submitting the same prediction", async () => {
        const res = await request(app).post("/predictions/")
                .send({
                    matchId: 1,
                    home: 2,
                    away: 2
                })
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(400);
        expect(res.body.error).toBe("Error adding prediction");
    });

    it("updates an existing prediction", async () => {
        const res = await request(app).patch("/predictions/")
                .send({
                    matchId: 1,
                    home: 2,
                    away: 2
                })
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(201);
        expect(res.body.user_id).toBe(user_id);
        expect(res.body.match_id).toBe(1);
        expect(res.body.home).toBe(2);
        expect(res.body.away).toBe(2);
    });
});