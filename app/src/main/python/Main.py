from numpy import random
import numpy as np
import io
import matplotlib.pyplot as plt
import shapely
from shapely.geometry import Point
from geopy.distance import geodesic

def hello():
  return "Hello, World!"

def generate_random():
    return random.randint(100)

def plot():
    xpoints = np.array([0, 2, 4, 6, 8, 10])
    ypoints = np.array([9, 24, 15, 30, 18, 45])

    fig, ax = plt.subplots()
    ax.plot(xpoints, ypoints, marker='o')

    f = io.BytesIO()
    plt.savefig(f, format="png")
    return f.getvalue()

def generate_shape():
    patch = Point(0.0, 0.0).buffer(10.0)
    return patch

def calculate_area(polygon):
    return polygon.area

def distance():
    newport_ri = (41.49008, -71.312796)
    cleveland_oh = (41.499498, -81.695391)
    return geodesic(newport_ri, cleveland_oh).miles