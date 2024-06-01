import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import { scoreController } from "../controllers/scoreController";

const router = Router();

router.post("/calculate-match-scores", scoreController.calculateScoresForMatch);

router.get("/user/:season", requireAuth, scoreController.getUserScoresBySeason);

router.get("/user/total/:season", requireAuth, scoreController.getTotalUserScoreBySeason);

export default router;