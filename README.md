CEC
===

> Have fun :)

Example
--------------

```java
CEC cec = new CEC();
cec.setData(".. filepath ..", 
		"text/space-separated-values");
//All Gaussian distributions
cec.add(ClusterKind.Gaussians, 3);
//Gaussians with sepcified eigenvalues of covariance matrix
cec.add(ClusterKind.LambdaGaussians, 3,
		TypeOption.add("lambda", new double[]{1., 0.1}));
//Gaussians with sepcified covariance matrix
cec.add(ClusterKind.CovarianceGaussians, 3,
		TypeOption.add("covariance", new double[][]{{1., 0.1}, {0.1, 1}}));
//Gaussians with a covaraince matrix with given determinant
cec.add(ClusterKind.DeterminantGaussians, 3,
		TypeOption.add("det", 1.5)
);
//Gaussians with diagonal covaraince
cec.add(ClusterKind.DiagonalGaussians, 3);
//Spherical Gaussians: radial Gaussian densities
cec.add(ClusterKind.SphericalGaussians, 3);
//Spherical Gaussians with a fixed radius: radial Gaussian densities
cec.add(ClusterKind.SphericalGaussiansWithFixedRadius, 3,
		TypeOption.add("r", 0.5)
);

cec.run();

//print the results
//and if it possible you will see the plot
cec.showResults();

//save results to file
cec.saveResults();

new DataDraw(cec.getResult()).disp(); 
```
For more example see the package ```cec.test``` 
Addtional configuration is dune by the file ```cecconfig.properties```

Simple Example
--------------
![alt tag](https://raw.github.com/kmisztal/CEC/master/img/mouse.png)


TODO
----


Version
----

0.1 Basic version of the software


License
----

MIT


**Free Software, Hell Yeah!**