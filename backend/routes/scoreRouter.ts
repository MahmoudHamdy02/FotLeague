import { Router } from "express";
import * as scoreController from "../controllers/scoreController";
import { requireAuth } from "../middleware/requireAuth";

const router = Router();

router.post("/calculate-match-scores", scoreController.calculateScoresForMatch);

router.get("/user/:season", requireAuth, scoreController.getUserScoresBySeason);

export default router;