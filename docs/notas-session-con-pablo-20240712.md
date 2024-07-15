
## Arquitectura propuesta en el primer refactor

```mermaid
flowchart LR
    subgraph LR [Chat Manager]
        chat_manager --uses--> chat_agent
        Telegram     --implements--> chat_agent
        WhatsApp     --implements--> chat_agent

        chat_manager -.runtime dependency.-> Telegram
        chat_manager -.runtime dependency.-> WhatsApp
    end
```

The EFS:

```
tdlib/
  gocentius/
    instance-1/
    instance-2/
  eadtrust/
    instance-1/
    instance-2/
```

No es posible porque no puede haber N clietes de Telegram activos en diferentes nodos, ya que dispararían los eventos que se reciben de Telegram varias veces.

## Arquitectura nueva


```mermaid
flowchart LR

    events[Events]
    chatManager[Chat Manager]

    chatManager[Chat Manager] --commands--> chatAgent
    chatAgent --events--> chatManager

    chatAgent --> tClient[Telegram Client]
    tClient   --> chatAgent
    chatAgent --> wClient[WhatsApp Client]
    wClient   --> chatAgent

    %% dependencies
    chatManager -.-> events
    chatAgent   -.-> events
    chatAgent   -.runtime.-> tClient
    chatAgent   -.runtime.-> wClient
    tClient   -.-> events
    wClient   -.-> events
```

The EFS:

```
tdlib/
  gocentius/
  eadtrust/
```

Puntos a tener en cuenta:

* Puede haber **N agentes de chat** (Telegram, WhatsApp)
* Un agente puede dar servicio N tenants

