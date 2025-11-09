import numpy as np
import matplotlib.pyplot as plt

# Membership functions
def triangular(x, a, b, c):
    return np.maximum(np.minimum((x - a)/(b - a), (c - x)/(c - b)), 0)

def trapezoidal(x, a, b, c, d):
    return np.maximum(np.minimum(np.minimum((x - a)/(b - a), 1), (d - x)/(d - c)), 0)

def gaussian(x, mean, sigma):
    return np.exp(-0.5 * ((x - mean) / sigma)**2)

def sigmoid(x, a, c):
    return 1 / (1 + np.exp(-a * (x - c)))

def bell(x, a, b, c):
    return 1 / (1 + np.abs((x - c) / a)**(2 * b))

x = np.linspace(-10, 10, 500)

# triangular membership function
fig, axs = plt.subplots(1, 3, figsize=(15, 4))
fig.suptitle("Changes in Triangular Membership Function")

# Original
axs[0].plot(x, triangular(x, 2, 5, 8), label="Original (a=2, b=5, c=8)")
axs[0].set_title("Original")
axs[0].legend()
axs[0].grid()

# Change peak b
axs[1].plot(x, triangular(x, 2, 5, 8), label="Original (b=5)", linestyle="--")
axs[1].plot(x, triangular(x, 2, 7, 8), label="b=7")
axs[1].set_title("Effect of Changing Peak 'b'")
axs[1].legend()
axs[1].grid()

# Change width a & c
axs[2].plot(x, triangular(x, 2, 5, 8), label="Original (a=2, c=8)", linestyle="--")
axs[2].plot(x, triangular(x, 3, 5, 7), label="a=3, c=7")
axs[2].set_title("Effect of Changing Width 'a' & 'c'")
axs[2].legend()
axs[2].grid()

plt.tight_layout(rect=[0, 0.03, 1, 0.95])
plt.show()

# trapezoidal membership function
fig, axs = plt.subplots(1, 3, figsize=(15, 4))
fig.suptitle("Changes in Trapezoidal Membership Function")

# Original
axs[0].plot(x, trapezoidal(x, -6, -3, 3, 6), label="Original (a=-6, b=-3, c=3, d=6)")
axs[0].set_title("Original")
axs[0].legend()
axs[0].grid()

# Change b and c
axs[1].plot(x, trapezoidal(x, -6, -3, 3, 6), label="Original", linestyle="--")
axs[1].plot(x, trapezoidal(x, -6, -2, 2, 6), label="b=-2, c=2")
axs[1].set_title("Effect of Changing 'b' & 'c'")
axs[1].legend()
axs[1].grid()

# Change a and d
axs[2].plot(x, trapezoidal(x, -6, -3, 3, 6), label="Original", linestyle="--")
axs[2].plot(x, trapezoidal(x, -5, -3, 3, 5), label="a=-5, d=5")
axs[2].set_title("Effect of Changing 'a' & 'd'")
axs[2].legend()
axs[2].grid()

plt.tight_layout(rect=[0, 0.03, 1, 0.95])
plt.show()

# gaussian membership function
fig, axs = plt.subplots(1, 3, figsize=(15, 4))
fig.suptitle("Changes in Gaussian Membership Function")

# Original
axs[0].plot(x, gaussian(x, 0, 2), label="Original (mean=0, sigma=2)")
axs[0].set_title("Original")
axs[0].legend()
axs[0].grid()

# Change mean
axs[1].plot(x, gaussian(x, 0, 2), label="Original", linestyle="--")
axs[1].plot(x, gaussian(x, 2, 2), label="mean=2")
axs[1].set_title("Effect of Changing Mean")
axs[1].legend()
axs[1].grid()

# Change sigma
axs[2].plot(x, gaussian(x, 0, 2), label="Original", linestyle="--")
axs[2].plot(x, gaussian(x, 0, 3), label="sigma=3")
axs[2].set_title("Effect of Changing Sigma")
axs[2].legend()
axs[2].grid()

plt.tight_layout(rect=[0, 0.03, 1, 0.95])
plt.show()

#sigmoid membership function
fig, axs = plt.subplots(1, 3, figsize=(15, 4))
fig.suptitle("Changes in Sigmoid Membership Function")

# Original
axs[0].plot(x, sigmoid(x, 1, 0), label="Original (a=1, c=0)")
axs[0].set_title("Original")
axs[0].legend()
axs[0].grid()

# Change c
axs[1].plot(x, sigmoid(x, 1, 0), label="Original", linestyle="--")
axs[1].plot(x, sigmoid(x, 1, 2), label="c=2")
axs[1].set_title("Effect of Changing 'c'")
axs[1].legend()
axs[1].grid()

# Change a
axs[2].plot(x, sigmoid(x, 1, 0), label="Original", linestyle="--")
axs[2].plot(x, sigmoid(x, 3, 0), label="a=3")
axs[2].set_title("Effect of Changing 'a'")
axs[2].legend()
axs[2].grid()

plt.tight_layout(rect=[0, 0.03, 1, 0.95])
plt.show()

# generalized bell membership function
fig, axs = plt.subplots(1, 3, figsize=(15, 4))
fig.suptitle("Changes in Generalized Bell Membership Function")

# Original
axs[0].plot(x, bell(x, 2, 3, 0), label="Original (a=2, b=3, c=0)")
axs[0].set_title("Original")
axs[0].legend()
axs[0].grid()

# Change c
axs[1].plot(x, bell(x, 2, 3, 0), label="Original", linestyle="--")
axs[1].plot(x, bell(x, 2, 3, 2), label="c=2")
axs[1].set_title("Effect of Changing 'c'")
axs[1].legend()
axs[1].grid()

# Change a and b
axs[2].plot(x, bell(x, 2, 3, 0), label="Original", linestyle="--")
axs[2].plot(x, bell(x, 1, 2, 0), label="a=1, b=2")
axs[2].set_title("Effect of Changing 'a' & 'b'")
axs[2].legend()
axs[2].grid()

plt.tight_layout(rect=[0, 0.03, 1, 0.95])
plt.show()
