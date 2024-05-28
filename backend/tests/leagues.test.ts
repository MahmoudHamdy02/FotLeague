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

describe("League System", () => {
    let cookie: string;

    const newUser = {
        email: "test@gmail.com",
        password: "test123",
        name: "testuser",
        role: 2
    };

    const newUser2 = {
        email: "test2@gmail.com",
        password: "test123",
        name: "testuser2",
        role: 2
    };

    const newLeague = {
        name: "Test League"
    };
    let newLeagueCode: string;
    let newLeagueId: number;

    beforeAll(async () => {
        const res = await request(app).post("/auth/signup")
                .send(newUser);
        cookie = res.headers["set-cookie"][0];
    });

    it("creates a new league", async () => {
        const res = await request(app).post("/leagues/")
                .send(newLeague)
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(201);
        expect(typeof res.body.id).toEqual("number");
        expect(typeof res.body.code).toEqual("string");
        expect(res.body.code.length).toEqual(6);
        expect(res.body.name).toEqual(newLeague.name);
        newLeagueCode = res.body.code;
        newLeagueId = res.body.id;
    });

    it("joins a new league", async () => {
        const res = await request(app).post("/auth/signup")
                .send(newUser2);
        cookie = res.headers["set-cookie"][0];

        const res2 = await request(app).post("/leagues/join")
                .send({code: newLeagueCode})
                .set("Cookie", cookie);
        expect(res2.statusCode).toEqual(201);
        expect(res2.body.league_id).toEqual(newLeagueId);
        expect(res2.body.user_id).toEqual(res.body.id);
    });

    it("throws an error when joining same league twice", async () => {
        const res = await request(app).post("/leagues/join")
                .send({code: newLeagueCode})
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(400);
        expect(res.body.error).toEqual("User already in league");
    });

    it("throws an error when joining using a wrong code", async () => {
        // Double check in case the created league has the same code
        const wrongCode = newLeagueCode === "111111"
            ? "222222"
            : "111111";
        const res = await request(app).post("/leagues/join")
                .send({code: wrongCode})
                .set("Cookie", cookie);
        expect(res.statusCode).toEqual(400);
        expect(res.body.error).toEqual("No league found");
    });
});