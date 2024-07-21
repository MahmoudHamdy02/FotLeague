import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import { leagueController } from "../controllers/league.controller";

export const leagueRouter = Router();

leagueRouter.get("/:id", requireAuth, leagueController.getLeagueDetailsById);

leagueRouter.post("/", requireAuth, leagueController.createLeague);

leagueRouter.post("/join/", requireAuth, leagueController.joinLeague);

leagueRouter.get("/user/leagues", requireAuth, leagueController.getUserLeagues);

leagueRouter.post("/leave", requireAuth, leagueController.leaveLeague);

leagueRouter.delete("/:id", requireAuth, leagueController.deleteLeague);
