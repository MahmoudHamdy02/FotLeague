import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import { leagueController } from "../controllers/league.controller";

export const leagueRouter = Router();

leagueRouter.post("/", requireAuth, leagueController.createLeague);

leagueRouter.post("/join/", requireAuth, leagueController.joinLeague);

leagueRouter.get("/user/leagues", requireAuth, leagueController.getUserLeagues);
