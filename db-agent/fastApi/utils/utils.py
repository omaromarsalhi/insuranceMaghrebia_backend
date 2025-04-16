import json
from dataclasses import dataclass, asdict
from typing import Optional, List
from datetime import datetime, date, time
import uuid
import decimal
import base64
import enum
import numpy as np

# --------------------------
# DTO Classes
# --------------------------
@dataclass
class Point:
    lat: Optional[float] = None
    lng: Optional[float] = None

@dataclass
class BoundingBox:
    northEast: Optional[Point] = None
    southWest: Optional[Point] = None

@dataclass
class LocationDto:
    boundingBox: Optional[BoundingBox] = None
    country: Optional[str] = None
    countryCode: Optional[str] = None
    countryCodeISO3: Optional[str] = None
    countrySubdivision: Optional[str] = None
    countrySubdivisionCode: Optional[str] = None
    countrySubdivisionName: Optional[str] = None
    extendedPostalCode: Optional[str] = None
    freeformAddress: Optional[str] = None
    localName: Optional[str] = None
    municipality: Optional[str] = None
    postalCode: Optional[str] = None
    streetName: Optional[str] = None
    streetNumber: Optional[str] = None

@dataclass
class GeneratedQuoteDto:
    amount: Optional[float] = None
    baseAnnualPremium: Optional[float] = None
    billingPeriod: Optional[str] = None
    currency: Optional[str] = None
    validFrom: Optional[str] = None
    validTo: Optional[str] = None

@dataclass
class AutomobileDto:
    accidentHistory: Optional[str] = None
    addressInfo: Optional[LocationDto] = None
    coverageType: Optional[str] = None
    defensiveDrivingCourse: Optional[bool] = None
    drivingExperience: Optional[int] = None
    licenseNumber: Optional[str] = None
    trafficViolations: Optional[bool] = None
    vehicleMake: Optional[str] = None
    vehicleModel: Optional[str] = None
    vehicleType: Optional[str] = None
    vin: Optional[str] = None

@dataclass
class AppointmentDto:
    cin: Optional[int] = None
    dob: Optional[str] = None
    email: Optional[str] = None
    firstName: Optional[str] = None
    generatedQuote: Optional[GeneratedQuoteDto] = None
    lastName: Optional[str] = None
    offerDetails: Optional[AutomobileDto] = None
    offerType: Optional[str] = None
    phone: Optional[int] = None

# --------------------------
# Serialization & Transformation
# --------------------------
def transform_to_dto(raw_data: dict) -> AppointmentDto:
    """Convert raw database row to structured DTO"""
    location = LocationDto(
        boundingBox=BoundingBox(
            northEast=Point(
                lat=raw_data.get('north_east_lat'),
                lng=raw_data.get('north_east_lng')
            ),
            southWest=Point(
                lat=raw_data.get('south_west_lat'),
                lng=raw_data.get('south_west_lng')
            )
        ),
        country=raw_data.get('country'),
        countryCode=raw_data.get('country_code'),
        countryCodeISO3=raw_data.get('country_codeiso3'),
        countrySubdivision=raw_data.get('country_subdivision'),
        freeformAddress=raw_data.get('freeform_address'),
        municipality=raw_data.get('municipality'),
        postalCode=raw_data.get('postal_code'),
        streetName=raw_data.get('street_name'),
        streetNumber=raw_data.get('street_number')
    )

    automobile = AutomobileDto(
        accidentHistory=raw_data.get('accident_history'),
        addressInfo=location,
        coverageType=raw_data.get('coverage_type'),
        defensiveDrivingCourse=raw_data.get('defensive_driving_course'),
        drivingExperience=raw_data.get('driving_experience'),
        licenseNumber=raw_data.get('license_number'),
        trafficViolations=raw_data.get('traffic_violations'),
        vehicleMake=raw_data.get('vehicle_make'),
        vehicleModel=raw_data.get('vehicle_model'),
        vehicleType=raw_data.get('vehicle_type')
    )

    quote = GeneratedQuoteDto(
        amount=raw_data.get('amount'),
        baseAnnualPremium=raw_data.get('base_annual_premium'),
        billingPeriod=raw_data.get('billing_period'),
        currency=raw_data.get('currency'),
        validFrom=raw_data.get('valid_from'),
        validTo=raw_data.get('valid_to')
    )

    return AppointmentDto(
        cin=raw_data.get('cin'),
        dob=raw_data.get('dob'),
        email=raw_data.get('email'),
        firstName=raw_data.get('first_name'),
        generatedQuote=quote,
        lastName=raw_data.get('last_name'),
        offerDetails=automobile,
        offerType=raw_data.get('offer_type'),
        phone=raw_data.get('phone')
    )

def serialize(query_result):
    """Enhanced serialization with DTO transformation"""
    result = []

    for item in query_result:
        # First convert database types to serializable formats
        processed_item = {}
        for key, value in item.items():
            if isinstance(value, (datetime, date, time)):
                processed_item[key] = value.isoformat()
            elif isinstance(value, uuid.UUID):
                processed_item[key] = str(value)
            elif isinstance(value, decimal.Decimal):
                processed_item[key] = float(value)
            elif isinstance(value, bytes):
                processed_item[key] = base64.b64encode(value).decode('utf-8')
            elif isinstance(value, enum.Enum):
                processed_item[key] = value.name
            elif isinstance(value, (np.int64, np.float32, np.float64)):
                processed_item[key] = value.item()
            elif hasattr(value, '__table__'):
                processed_item[key] = {
                    col.name: getattr(value, col.name)
                    for col in value.__table__.columns
                }
            else:
                processed_item[key] = value

        # Then transform to DTO structure
        dto = transform_to_dto(processed_item)
        result.append(asdict(dto))

    return result
    # return json.dumps(result, default=str)

# Example usage:
# raw_data = db_session.execute(query).fetchall()
# serialized = serialize(raw_data)  # Returns JSON string in DTO format


# import json
#
# from llama_index.llms.gemini import Gemini
#
# import datetime
# import uuid
# import decimal
# import base64
# import enum
# import numpy as np
#
#
#
# def serialize(query_result):
#     for item in query_result:
#         for key in list(item.keys()):
#             value = item[key]
#
#             if isinstance(value, (datetime.datetime, datetime.date, datetime.time)):
#                 item[key] = value.isoformat()
#
#             elif isinstance(value, uuid.UUID):
#                 item[key] = str(value)
#
#             elif isinstance(value, decimal.Decimal):
#                 item[key] = float(value)
#
#             elif isinstance(value, bytes):
#                 item[key] = base64.b64encode(value).decode('utf-8')
#
#             elif isinstance(value, enum.Enum):
#                 item[key] = value.name
#
#             elif isinstance(value, (np.int64, np.float32, np.float64)):
#                 item[key] = value.item()
#
#             elif hasattr(value, '__table__'):
#                 item[key] = {
#                     col.name: getattr(value, col.name)
#                     for col in value.__table__.columns
#                 }
#
#             elif isinstance(value, (list, dict)):
#                 # Recursive serialization for nested structures
#                 item[key] = json.loads(json.dumps(value, default=str))
#     return query_result