import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import * as leagueController from "../controllers/leagueController";

const router = Router();

router.post("/", requireAuth, leagueController.createLeague);

router.post("/join/", requireAuth, leagueController.joinLeague);

export default router;