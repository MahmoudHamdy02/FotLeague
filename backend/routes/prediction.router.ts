import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import { predictionController } from "../controllers/prediction.controller";
import { predictionValidator } from "../validators/prediction.validator";
import { validate } from "../validators/validate";
import { validator } from "../validators/validator";

export const predictionRouter = Router();

predictionRouter.get("/", requireAuth, predictionController.getUserPredictionsForCurrentSeason);

predictionRouter.get("/:season", requireAuth, validator.season, validate, predictionController.getUserPredictionsBySeason);

predictionRouter.post("/", requireAuth, predictionValidator.addPrediction, validate, predictionController.addPrediction);

predictionRouter.patch("/", requireAuth, predictionValidator.updatePrediction, validate, predictionController.updatePrediction);
