import { Router } from "express";
import { requireAuth } from "../middleware/requireAuth";
import { predictionController } from "../controllers/predictionController";

const router = Router();

router.get("/:season", requireAuth, predictionController.getUserPredictionsBySeason);

router.post("/", requireAuth, predictionController.addPrediction);

router.patch("/", requireAuth, predictionController.updatePrediction);

export default router;