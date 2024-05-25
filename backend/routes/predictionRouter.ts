import { Router } from "express";
import * as predictionController from "../controllers/predictionController";

const router = Router();

router.get("/:season", predictionController.getUserPredictionsBySeason);

router.post("/");

router.patch("/:id");

export default router;