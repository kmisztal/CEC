CEC
===

> Have fun :)

Example
--------------

```java
CEC cec = new CEC();
cec.setData(".. filepath ..", 
		"text/space-separated-values");

cec.add(ClusterKind.Gaussians, 3);

cec.add(ClusterKind.LambdaGaussians, 3,
		TypeOption.add("lambda", new double[]{1., 0.1}));

cec.add(ClusterKind.CovarianceGaussians, 3,
		TypeOption.add("covariance", new double[][]{{1., 0.1}, {0.1, 1}}));

cec.add(ClusterKind.DeterminantGaussians, 3,
		TypeOption.add("det", 1.5)
);

cec.add(ClusterKind.DiagonalGaussians, 3);

cec.add(ClusterKind.SphericalGaussians, 3);

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

Version
----

0.1


License
----

MIT


**Free Software, Hell Yeah!**