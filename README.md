CEC
===

This project performs clustering analysis based on the cross-entropy clustering (CEC) algorithm, which was recently developed with the use of information theory. 
The main advantages of CEC is that it automatically reduces unnecessary clusters while combining the speed and simplicity of k-means with the ability to 
easily use various gaussian mixture models.

In this work we provide a JAVA implementation of the \proglang{R} Package \pkg{CEC} which would be soon / which is avalible on CRAN.


> **NOTE:** If you want to use our software in your reasearch please cite our articles:
>
> - Tabor, Jacek, and Przemysław Spurek.   
>   "Cross-entropy clustering."  
>   Pattern Recognition 47.9 (2014): 3046-3059.
> - Tabor, Jacek, and Krzysztof Misztal.  
>   "Detection of elliptical shapes via cross-entropy clustering."  
>   Pattern Recognition and Image Analysis. Springer Berlin Heidelberg, 2013. 656-663.

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
//Gaussians with specified covariance matrix
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
Additional configuration is dune by the file ```cecconfig.properties```

Simple Example
--------------
![alt tag](https://raw.github.com/kmisztal/CEC/master/src/main/resources/img/mouse.png)

TODO
----


Version
----

0.1 Basic version of the software


License
----

MIT


**Free Software, Hell Yeah!**
