import express from "express";
import cors from "cors";

const app = express();
app.use(cors());
app.use(express.json({ limit: "2mb" }));

app.get("/v1/health/ping", (_req, res) => {
  res.json({ ok: true, service: "healthscan-ai", time: Date.now() });
});

app.post("/v1/sync", (req, res) => {
  const body = req.body || {};
  console.log("Received sync payload:", {
    deviceId: body.deviceId,
    vitals: body.vitals?.length ?? 0,
    symptoms: body.symptoms?.length ?? 0,
    labs: body.labs?.length ?? 0,
    findings: body.findings?.length ?? 0
  });

  // DEVELOPMENT ONLY:
  // Replace this with authenticated storage, encryption at rest,
  // audit logging, retention controls and appropriate compliance work.
  res.json({ ok: true, receivedAt: Date.now() });
});

app.listen(process.env.PORT || 8080, () => {
  console.log(`HealthScan AI server listening on ${process.env.PORT || 8080}`);
});
