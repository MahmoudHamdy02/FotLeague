import { Router } from "express";
import * as predictionController from "../controllers/predictionController";
import { requireAuth } from "../middleware/requireAuth";

const router = Router();

router.get("/:season", requireAuth, predictionController.getUserPredictionsBySeason);

router.post("/", requireAuth, predictionController.addPrediction);

router.patch("/", requireAuth, predictionController.updatePrediction);

export default router;