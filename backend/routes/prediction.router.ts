import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import { predictionController } from "../controllers/prediction.controller";

export const predictionRouter = Router();

predictionRouter.get("/:season", requireAuth, predictionController.getUserPredictionsBySeason);

predictionRouter.post("/", requireAuth, predictionController.addPrediction);

predictionRouter.patch("/", requireAuth, predictionController.updatePrediction);
