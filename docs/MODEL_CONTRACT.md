# ML model contract

Every medical screening model added to this app should expose:

- model name
- model version
- intended use
- input requirements
- output labels
- validation population
- confidence/calibration information
- known limitations
- contraindications/exclusions
- model checksum

The Android layer should never convert an arbitrary model output into a disease diagnosis.

Preferred flow:

image/signal
-> quality gate
-> validated model
-> structured observation
-> safety/rules layer
-> user-facing explanation
-> appropriate next step
