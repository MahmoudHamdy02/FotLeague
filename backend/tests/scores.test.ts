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

describe("Score System", () => {
    let cookie: string;

    const newUser = {
        email: "test@gmail.com",
        password: "test123",
        name: "testuser",
        role: 2
    };
    let user_id: number;

    beforeAll(async () => {
        const res = await request(app).post("/auth/signup")
                .send(newUser);
        user_id = res.body.id;
        cookie = res.headers["set-cookie"][0];

        // Test setup: 5 matches, 1 exact prediction, 3 correct results, one wrong prediction
        await pool.query("INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek) VALUES ('Liverpool', 'Everton', 3, 1, 3, '2024-12-14 18:00:00', 2024, 1);");
        await pool.query("INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek) VALUES ('Liverpool', 'Man City', 3, 3, 3, '2024-12-14 18:00:00', 2024, 1);");
        await pool.query("INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek) VALUES ('Liverpool', 'Man United', 2, 0, 3, '2024-12-14 18:00:00', 2024, 1);");
        await pool.query("INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek) VALUES ('Liverpool', 'Chelsea', 0, 2, 3, '2024-12-14 18:00:00', 2024, 1);");
        await pool.query("INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek) VALUES ('Liverpool', 'Arsenal', 1, 0, 3, '2024-12-14 18:00:00', 2024, 1);");
        await request(app).post("/predictions/")
                .send({
                    user_id: user_id,
                    match_id: 1,
                    home: 3,
                    away: 1
                })
                .set("Cookie", cookie);
        await request(app).post("/predictions/")
                .send({
                    user_id: user_id,
                    match_id: 2,
                    home: 2,
                    away: 2
                })
                .set("Cookie", cookie);
        await request(app).post("/predictions/")
                .send({
                    user_id: user_id,
                    match_id: 3,
                    home: 1,
                    away: 0
                })
                .set("Cookie", cookie);
        await request(app).post("/predictions/")
                .send({
                    user_id: user_id,
                    match_id: 4,
                    home: 0,
                    away: 1
                })
                .set("Cookie", cookie);
        await request(app).post("/predictions/")
                .send({
                    user_id: user_id,
                    match_id: 5,
                    home: 0,
                    away: 0
                })
                .set("Cookie", cookie);
    });

    it("returns empty score list for new users", async () => {
        const res = await request(app).get("/scores/user/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(res.body.length).toEqual(0);
        expect(res.body).toEqual([]);
    });

    it("awards score of 3 for exact prediction", async () => {
        await request(app).post("/scores/calculate-match-scores")
                .send({
                    matchId: 1,
                })
                .set("Cookie", cookie);

        const res = await request(app).get("/scores/user/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(res.body.length).toEqual(1);
        expect(res.body[0]).toEqual({
            user_id: user_id,
            match_id: 1,
            score: 3
        });
    });

    it("updates user's season score to 3", async () => {
        const res = await request(app).get("/scores/user/total/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(typeof res.body.score).toEqual("number");
        expect(res.body.score).toEqual(3);
    });

    it("awards score of 1 for correct result: draw", async () => {
        await request(app).post("/scores/calculate-match-scores")
                .send({
                    matchId: 2,
                })
                .set("Cookie", cookie);

        const res = await request(app).get("/scores/user/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(res.body.length).toEqual(2);
        expect(res.body[1]).toEqual({
            user_id: user_id,
            match_id: 2,
            score: 1
        });
    });

    it("updates user's season score to 4", async () => {
        const res = await request(app).get("/scores/user/total/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(typeof res.body.score).toEqual("number");
        expect(res.body.score).toEqual(4);
    });

    it("awards score of 1 for correct result: home win", async () => {
        await request(app).post("/scores/calculate-match-scores")
                .send({
                    matchId: 3,
                })
                .set("Cookie", cookie);

        const res = await request(app).get("/scores/user/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(res.body.length).toEqual(3);
        expect(res.body[2]).toEqual({
            user_id: user_id,
            match_id: 3,
            score: 1
        });
    });

    it("updates user's season score to 5", async () => {
        const res = await request(app).get("/scores/user/total/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(typeof res.body.score).toEqual("number");
        expect(res.body.score).toEqual(5);
    });

    it("awards score of 1 for correct result: away win", async () => {
        await request(app).post("/scores/calculate-match-scores")
                .send({
                    matchId: 4,
                })
                .set("Cookie", cookie);

        const res = await request(app).get("/scores/user/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(res.body.length).toEqual(4);
        expect(res.body[3]).toEqual({
            user_id: user_id,
            match_id: 4,
            score: 1
        });
    });

    it("updates user's season score to 6", async () => {
        const res = await request(app).get("/scores/user/total/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(typeof res.body.score).toEqual("number");
        expect(res.body.score).toEqual(6);
    });

    it("awards score of 0 for wrong prediction", async () => {
        await request(app).post("/scores/calculate-match-scores")
                .send({
                    matchId: 5,
                })
                .set("Cookie", cookie);

        const res = await request(app).get("/scores/user/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(res.body.length).toEqual(5);
        expect(res.body[4]).toEqual({
            user_id: user_id,
            match_id: 5,
            score: 0
        });
    });

    it("keeps user's season score at 6", async () => {
        const res = await request(app).get("/scores/user/total/2024")
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(200);
        expect(typeof res.body.score).toEqual("number");
        expect(res.body.score).toEqual(6);
    });
});