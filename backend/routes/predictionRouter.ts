import { Router } from "express";
import * as predictionController from "../controllers/predictionController";

const router = Router();

router.get("/:season", predictionController.getUserPredictionsBySeason);

router.post("/", predictionController.addPrediction);

router.patch("/", predictionController.updatePrediction);

export default router;