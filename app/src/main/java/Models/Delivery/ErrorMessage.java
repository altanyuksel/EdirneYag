package Models.Delivery;

public class ErrorMessage {
    enum ErrType {
        PRODUCT_CODE,
        CUSTOMER_CODE,
        WAREHOUSE_CODE,
        USER_CODE,
        REGION_CODE,
        WAYBILL_NO,
        CONTRACT_NO,
        OTHERS,
        INVALID_PRODUCT_CODE,
        CONTRACT_NO_INV_PRD,
        CONTRACT_NO_INV_CUS,
        CONTRACT_USING,
        CONTRACT_EXISTS,
        VEHICLE_EXISTS,
        VEHICLE_TYPE_INVALID,
        VEHICLE_STATUS_INVALID,
        VEHICLE_NO_INVALID
    }

    public ErrType get_errCode() {
        return _errCode;
    }
    public void set_errCode(ErrType _errCode) {
        this._errCode = _errCode;
    }
    private ErrType _errCode;

    public String ErrDescription(){
        return GetErrDesc(_errCode);
    }

    private final String GetErrDesc(ErrType errType) {
        String errDesc = "";
        switch (errType) {
            case PRODUCT_CODE:
                errDesc = "Invalid product code!";
                break;
            case CUSTOMER_CODE:
                errDesc = "Invalid customer code!";
                break;
            case WAREHOUSE_CODE:
                errDesc = "Invalid warehouse code!";
                break;
            case USER_CODE:
                errDesc = "Invalid user code!";
                break;
            case REGION_CODE:
                errDesc = "Invalid region code!";
                break;
            case WAYBILL_NO:
                errDesc = "Invalid waybill no!";
                break;
            case CONTRACT_NO:
                errDesc = "Invalid contract no!";
                break;
            case INVALID_PRODUCT_CODE:
                errDesc = "Invalid product code, product code must be agricultural raw materials! ";
                break;
            case CONTRACT_NO_INV_PRD:
                errDesc = "The product does not belong to the contract!";
                break;
            case CONTRACT_NO_INV_CUS:
                errDesc = "The customer does not belong to the contract!";
                break;
            case CONTRACT_USING:
                errDesc = "The contract is already using, it can\'t update!";
                break;
            case CONTRACT_EXISTS:
                errDesc = "The contract is not exists, it can\'t update!";
                break;
            case VEHICLE_EXISTS:
                errDesc = "The vehicle is not exists, it can\'t update!";
                break;
            case VEHICLE_TYPE_INVALID:
                errDesc = "The vehicle type is not \'Agricultural Raw Material Vehicle\', it can\'t update!";
                break;
            case VEHICLE_STATUS_INVALID:
                errDesc = "The vehicle status is invalid, it can\'t update!";
                break;
            case VEHICLE_NO_INVALID:
                errDesc = "The vehicle no is empty, it can\'t update!";
                break;
            default:
                errDesc = "Others errors!";
                break;
        }
        return errDesc;
    }
}