BSDS Assignment 1
Interface:  DiagnoseInterface, PublishInterface, SubscriberInterface
Client: CASubClient, CAPubClient, CADiagnose
Server: CAServer
Other: TimeOutRemover

Argument Instruction:
CAPubClient:
args[0]: localhost
args[1]: Number of threads;
args[2]: Topic 1
args[3]: Numbers of post for Topic1
args[4]: Topic 2……so on like that

CASubClient:
args[0]: localhost
args[1]: Topic 1
args[2]: Numbers of message asking for Topic1
args[3]: Topic 2……so on like that

CADiagnose:
args[0]: localhost
args[1]: Topic 1
args[2]: Topic 2……so on like that
