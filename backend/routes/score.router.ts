import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import { scoreController } from "../controllers/score.controller";

export const scoreRouter = Router();

scoreRouter.post("/calculate-match-scores", scoreController.calculateScoresForMatch);

scoreRouter.get("/user/:season", requireAuth, scoreController.getUserScoresBySeason);

scoreRouter.get("/user/total/:season", requireAuth, scoreController.getTotalUserScoreBySeason);

// TODO: Needs tests
scoreRouter.get("/top-10-global", scoreController.getTopGlobalUsersBySeason);
