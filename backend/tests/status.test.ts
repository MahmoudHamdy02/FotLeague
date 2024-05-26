import {describe, it, expect} from "@jest/globals";
import app from "../app";
import request from "supertest";


describe("Status Check", () => {
    it("should return OK", async () => {
        const res = await request(app).get("/");
        expect(res.statusCode).toBe(200);
        expect(res.body.message).toBe("Server is running");
    });
});