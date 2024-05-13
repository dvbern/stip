# Debugging

## Local
WIP

## Dev
WIP

## UAT
### DB
To debug UAT you often need access to the DB to run some queries, this can be achieved by running a pod in the cluster directly and attaching a tty to it. This is easy enough in OpenShift simply do:

    oc run -i --tty postgres-debug --image=postgres --restart=Never -- sh

Replacing the `image=...` part with whatever image you want to start. Note that Apollo only has access to our internal docker registry and can't directly access public registries. Remember to delete the pod after debugging by running

    oc delete pod postgres-debug
