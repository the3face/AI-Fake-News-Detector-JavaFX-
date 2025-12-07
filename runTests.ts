import { detectHeadline } from "./backend/fakeNewsDetector.ts";

const testHeadlines = [
  "Scientists discover a new mineral that grants immortality to humans",
  "Coffee proven to let people live forever in shocking new report",
  "New study claims humans can now breathe underwater without equipment",
  "Scientists confirm teleportation technology is ready for public use",
  "New evidence proves birds are actually government drones",
  "Secret island discovered where dinosaurs still live",
  "This viral method instantly doubles your income!",
  "BREAKING: New miracle drink discovered!!!",
  "Doctors guarantee this breathing technique cures anxiety instantly",

  // Trustworthy examples
  "Researchers at the University of Oxford publish new findings on sleep patterns",
  "Study published in a peer-reviewed journal shows moderate exercise improves memory",
  "Government report outlines improvements in national cybersecurity readiness",
];

console.log("=== TruthSense AI Testing Output ===\n");

for (const headline of testHeadlines) {
  const result = detectHeadline(headline);
  console.log(`Headline: ${headline}`);
  console.log(` → Label: ${result.label}`);
  console.log(` → Confidence: ${result.confidence}%`);
  console.log(` → Explanation: ${result.explanation}\n`);
}
