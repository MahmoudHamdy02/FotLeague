import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import { predictionController } from "../controllers/prediction.controller";
import { predictionValidator } from "../validators/prediction.validator";

export const predictionRouter = Router();

predictionRouter.get("/", requireAuth, predictionController.getUserPredictionsForCurrentSeason);

predictionRouter.get("/:season", requireAuth, predictionValidator.getUserPredictionsBySeason, predictionController.getUserPredictionsBySeason);

predictionRouter.post("/", requireAuth, predictionValidator.addPrediction, predictionController.addPrediction);

predictionRouter.patch("/", requireAuth, predictionValidator.updatePrediction, predictionController.updatePrediction);
