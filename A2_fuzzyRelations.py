import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

# --- Entities ---
cars = ["Tesla", "Honda", "BMW"]
roads = ["Mountain", "Highway", "Expressway"]

R = np.array([
    [0.7, 0.9, 0.8],  # Tesla
    [0.6, 0.7, 0.5],  # Honda
    [0.8, 0.8, 0.9]   # BMW
])

# S: Safety level of cars on roads
S = np.array([
    [0.6, 0.8, 0.7],  # Tesla
    [0.5, 0.6, 0.4],  # Honda
    [0.7, 0.9, 0.8]   # BMW
])

# --- Fuzzy Relational Operations ---
union_RS = np.maximum(R, S)         # Union (max)
intersection_RS = np.minimum(R, S)  # Intersection (min)
complement_R = 1 - R                # Complement of R

# --- Visualization: Original Fuzzy Relations ---
plt.figure(figsize=(12, 5))

plt.subplot(1, 2, 1)
sns.heatmap(R, annot=True, cmap="Blues", xticklabels=roads, yticklabels=cars, vmin=0, vmax=1)
plt.title("Fuzzy Relation R: Comfort Level")
plt.xlabel("Roads")
plt.ylabel("Cars")

plt.subplot(1, 2, 2)
sns.heatmap(S, annot=True, cmap="Oranges", xticklabels=roads, yticklabels=cars, vmin=0, vmax=1)
plt.title("Fuzzy Relation S: Safety Level")
plt.xlabel("Roads")
plt.ylabel("Cars")

plt.tight_layout()
plt.show()

# --- Visualization: Fuzzy Relational Operations ---
plt.figure(figsize=(18, 5))

plt.subplot(1, 3, 1)
sns.heatmap(union_RS, annot=True, cmap="Greens", xticklabels=roads, yticklabels=cars, vmin=0, vmax=1)
plt.title("Union (R ∪ S): Either Safe or Comfortable")
plt.xlabel("Roads")
plt.ylabel("Cars")

plt.subplot(1, 3, 2)
sns.heatmap(intersection_RS, annot=True, cmap="Reds", xticklabels=roads, yticklabels=cars, vmin=0, vmax=1)
plt.title("Intersection (R ∩ S): Safe and Comfortable")
plt.xlabel("Roads")
plt.ylabel("Cars")

plt.subplot(1, 3, 3)
sns.heatmap(complement_R, annot=True, cmap="Purples", xticklabels=roads, yticklabels=cars, vmin=0, vmax=1)
plt.title("Complement (R′): Discomfort Level")
plt.xlabel("Roads")
plt.ylabel("Cars")

plt.tight_layout()
plt.show()
