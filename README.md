# Decrease Color Depth Gray‑scale Image

This project focuses on processing grayscale images by dividing them into smaller segments, which leads to a decrease in the color depth.
To enhance the efficiency of the application, a multi‑threading and pipe‑based approach was implemented, enabling parallel processing of
the image segments and resulting in faster execution times and a more efficient workflow. Specifically, each image segment is processed in
a separate thread until the base image is fully reconstructed, after which it can be further processed. The resulting image is then transmitted
through pipes in segments to create the final image.
Each image segment is processed in a separate thread until the base image is fully reconstructed, after which it can be further processed. The
resulting image is then transmitted through pipes in segments to create the final image.
