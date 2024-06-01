import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import { leagueController } from "../controllers/leagueController";

const router = Router();

router.post("/", requireAuth, leagueController.createLeague);

router.post("/join/", requireAuth, leagueController.joinLeague);

router.get("/user/leagues", requireAuth, leagueController.getUserLeagues);

export default router;