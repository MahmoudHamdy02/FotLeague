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

// TODO: Needs more test, possibly move matches to json files
describe("Score System", () => {
    let cookie: string;

    const newUser = {
        email: "test@gmail.com",
        password: "test123",
        name: "testuser"
    };
    let user_id: number;

    const match1 = {
        homeScore: 3,
        awayScore: 1,
        status: 2,
        datetime: "2024-12-14 18:00:00"
    };
    const match2 = {
        homeScore: 3,
        awayScore: 3,
        status: 2,
        datetime: "2024-12-14 18:00:00"
    };
    const match3 = {
        homeScore: 2,
        awayScore: 0,
        status: 2,
        datetime: "2024-12-14 18:00:00"
    };
    const match4 = {
        homeScore: 0,
        awayScore: 2,
        status: 2,
        datetime: "2024-12-14 18:00:00"
    };
    const match5 = {
        homeScore: 1,
        awayScore: 0,
        status: 2,
        datetime: "2024-12-14 18:00:00"
    };

    beforeAll(async () => {
        const res = await request(app).post("/auth/signup")
                .send(newUser);
        user_id = res.body.id;
        cookie = res.headers["set-cookie"][0];


        // TODO: Refactor into one query?
        // Test setup: 5 matches, 1 exact prediction, 3 correct results, one wrong prediction
        await pool.query("INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek) VALUES ('Liverpool', 'Everton', $1, $2, $3, $4, 2024, 1);", [
            match1.homeScore,
            match1.awayScore,
            match1.status,
            match1.datetime
        ]);
        await pool.query("INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek) VALUES ('Liverpool', 'Man City', $1, $2, $3, $4, 2024, 1);", [
            match2.homeScore,
            match2.awayScore,
            match2.status,
            match2.datetime
        ]);
        await pool.query("INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek) VALUES ('Liverpool', 'Man United', $1, $2, $3, $4, 2024, 1);", [
            match3.homeScore,
            match3.awayScore,
            match3.status,
            match3.datetime
        ]);
        await pool.query("INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek) VALUES ('Liverpool', 'Chelsea', $1, $2, $3, $4, 2024, 1);", [
            match4.homeScore,
            match4.awayScore,
            match4.status,
            match4.datetime
        ]);
        await pool.query("INSERT INTO matches (home, away, home_score, away_score, match_status, datetime, season, gameweek) VALUES ('Liverpool', 'Arsenal', $1, $2, $3, $4, 2024, 1);", [
            match5.homeScore,
            match5.awayScore,
            match5.status,
            match5.datetime
        ]);

        // Exact prediction
        await request(app).post("/predictions/")
                .send({
                    user_id: user_id,
                    match_id: 1,
                    home: match1.homeScore,
                    away: match1.awayScore
                })
                .set("Cookie", cookie);
        // Draw
        await request(app).post("/predictions/")
                .send({
                    user_id: user_id,
                    match_id: 2,
                    home: match2.homeScore+1,
                    away: match2.awayScore+1
                })
                .set("Cookie", cookie);
        // Home win
        await request(app).post("/predictions/")
                .send({
                    user_id: user_id,
                    match_id: 3,
                    home: match3.homeScore+1,
                    away: match3.awayScore
                })
                .set("Cookie", cookie);
        // Away win
        await request(app).post("/predictions/")
                .send({
                    user_id: user_id,
                    match_id: 4,
                    home: match4.homeScore,
                    away: match4.awayScore+1
                })
                .set("Cookie", cookie);
        // Wrong result
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
        await request(app).post("/matches/update")
                .send({
                    matchId: 1,
                    status: 3,
                    homeScore: match1.homeScore,
                    awayScore: match1.awayScore,
                    datetime: match1.datetime
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
        await request(app).post("/matches/update")
                .send({
                    matchId: 2,
                    status: 3,
                    homeScore: match2.homeScore,
                    awayScore: match2.awayScore,
                    datetime: match2.datetime
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
        await request(app).post("/matches/update")
                .send({
                    matchId: 3,
                    status: 3,
                    homeScore: match3.homeScore,
                    awayScore: match3.awayScore,
                    datetime: match3.datetime
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
        await request(app).post("/matches/update")
                .send({
                    matchId: 4,
                    status: 3,
                    homeScore: match4.homeScore,
                    awayScore: match4.awayScore,
                    datetime: match4.datetime
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
        await request(app).post("/matches/update")
                .send({
                    matchId: 5,
                    status: 3,
                    homeScore: match5.homeScore,
                    awayScore: match5.awayScore,
                    datetime: match5.datetime
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