# qhana-workflow-low-code

This repository shows an example workflow orchestrating different QHAna plugins.

## Setting up the Environment

First, start the QHAna plugin runner using the docker setup provided [here](https://github.com/UST-QuAntiL/qhana-plugin-runner).

The Camunda workflow engine is dockerized and can be automatically started and initialized with the [example workflow](./workflow) using Docker-Compose:

1. Update the [.env](./.env) file with your settings: 
  * ``PUBLIC_HOSTNAME``: Enter the hostname/IP address of your Docker engine. Do *not* use ``localhost``.

2. Run the Docker-Compose file:
```
docker-compose pull
docker-compose up --build
```

## Running the Workflow

To execute the workflow, open ``$PUBLIC_HOSTNAME:8080`` after the Docker container has been started successfully to access the Camunda Engine UI:

1. Create a new user and login.

2. Open the ``Tasklist`` application which can be accessed by using the button in the top-right corner.

3. Click on ``Start process`` and select the ``quantum-workflow-demonstrator``.

4. Provide the required input parameters, e.g., the input data provided in this repository ``https://github.com/wederbn/qhana-workflow-low-code/blob/main/data/sym_max_mean.zip?raw=true``)

5. Start the workflow.

6. Switch to the ``Cockpit`` application to monitor the token flow and the current variables of the workflow instance.

7. Once the workflow instance reaches the final User Task, switch back to the ``Tasklist`` application and click on ``Add a simple filter`` on the left.

8. Afterwards, a task object is displayed. Select this task object and retrieve the final clustering results..

9. Finally, click on ``Complete`` to terminate the workflow instance.

10. To shut down the setup, run ``docker-compose down -v`` in the folder containing the Docker-Compose file.
