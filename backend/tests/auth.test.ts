import {describe, it, expect, afterAll} from "@jest/globals";
import app from "../app";
import request from "supertest";
import { pool } from "../db";

afterAll(async () => {
    await pool.end();
});

describe("Auth Tests", () => {
    it("should not access protected route when not signed in", async () => {
        const res = await request(app).get("/auth/status");
        expect(res.statusCode).toEqual(401);
        expect(res.body.error).toEqual("User is not authenticated");
    });

    it("should create a new user", async () => {
        const newUser = {
            email: "test@gmail.com",
            password: "test123",
            name: "testuser",
            role: 2
        };
        const res = await request(app).post("/auth/signup")
                .send(newUser);
        expect(res.statusCode).toEqual(201);
        expect(res.body.email).toEqual(newUser.email);
        expect(res.body.name).toEqual(newUser.name);
        expect(res.body.role).toEqual(newUser.role);
        expect(typeof res.body.id).toEqual("number");
        expect(res.body.password).not.toEqual(newUser.password);
    });
});