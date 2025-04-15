import json

from llama_index.llms.gemini import Gemini

import datetime
import uuid
import decimal
import base64
import enum
import numpy as np



def serialize(query_result):
    for item in query_result:
        for key in list(item.keys()):
            value = item[key]

            if isinstance(value, (datetime.datetime, datetime.date, datetime.time)):
                item[key] = value.isoformat()

            elif isinstance(value, uuid.UUID):
                item[key] = str(value)

            elif isinstance(value, decimal.Decimal):
                item[key] = float(value)

            elif isinstance(value, bytes):
                item[key] = base64.b64encode(value).decode('utf-8')

            elif isinstance(value, enum.Enum):
                item[key] = value.name

            elif isinstance(value, (np.int64, np.float32, np.float64)):
                item[key] = value.item()

            elif hasattr(value, '__table__'):
                item[key] = {
                    col.name: getattr(value, col.name)
                    for col in value.__table__.columns
                }

            elif isinstance(value, (list, dict)):
                # Recursive serialization for nested structures
                item[key] = json.loads(json.dumps(value, default=str))
    return query_result