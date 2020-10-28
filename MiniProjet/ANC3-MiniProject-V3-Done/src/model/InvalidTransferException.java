package model;

class InvalidTransferException extends RuntimeException{
    private final String error;
    
    public InvalidTransferException()
    {
        error = "Action impossible";
    }  
    
    public void stringToString()
    {
        System.out.println(error);
    }
}
