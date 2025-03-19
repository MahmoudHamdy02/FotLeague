import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import { scoreController } from "../controllers/score.controller";
import { validator } from "../validators/validator";
import { validate } from "../validators/validate";

export const scoreRouter = Router();

scoreRouter.get("/user", requireAuth, scoreController.getUserScores);

scoreRouter.get("/user/gameweeks", requireAuth, scoreController.getUserGameweekScores);

scoreRouter.get("/user/:season", requireAuth, validator.season, validate, scoreController.getUserScoresBySeason);

scoreRouter.get("/user/total/:season", requireAuth, validator.season, validate, scoreController.getTotalUserScoreBySeason);

// TODO: Needs tests
scoreRouter.get("/global", scoreController.getTopGlobalUsers);

scoreRouter.get("/highest", scoreController.getHighestGameweekScores);

scoreRouter.get("/average", scoreController.getAverageGameweekScores);
