# Open Notebook for Bioclipse scripts for WikiPathways

To use these scripts you need to install either
[Bioclipse 2.6.2](https://sourceforge.net/projects/bioclipse/files/bioclipse2/bioclipse2.6.2)
or [Bacting](https://github.com/egonw/bacting).

After that, you can run the scripts with Groovy. If you are not using Bacting, make sure
to remove the following lines at the top of the script:

```groovy
@Grab(group='io.github.egonw.managers', module='managers-cdk', version='0.0.5')

workspaceRoot = System.properties['user.dir']
cdk = new net.bioclipse.managers.CDKManager(workspaceRoot);
```
