package mySolution;

public class Option {
	
	String _name;
	int _startDate;
	int _expireDate;
	int _period;
	double _initialPrice;
	double _strickPrice;
	double _interestRate;
	double _volatility;
	String _type;
	
	 public static class OptionBuilder{
			private String _name;
			private int _startDate=20150904;
			private int _expireDate=20150904;
			private int _period=0;
			private double _initialPrice=0;
			private double _strickPrice=0;
			private double _interestRate=0.0001;
			private double _volatility=0;
			private String _type;
	 
	 
	 public OptionBuilder(){}
	 
	 public OptionBuilder setName(String name){
		 this._name=name;
		 return this;
	 }
	 
	 public OptionBuilder setPeriod(int period){
		 this._period=period;
		 this._expireDate=this._startDate+period;
		 return this;
	 
	 }
	
	 public OptionBuilder setInitialPrice(double price){
		 this._initialPrice=price;
		 return this;
	 }
	 
	 public OptionBuilder setStrickPrice(double price){
		 this._strickPrice=price;
		 return this;
	 }
	 
	 public OptionBuilder setInterestRate(double rate){
		 this._interestRate=rate;
		 return this;
	 }
	 
	 public OptionBuilder setVolatility(double var){
		 this._volatility=var;
		 return this;
	 }
	 
	 public OptionBuilder setType(String type){
		 this._type=type;
		 return this;
	 }
	 
     public Option build(){
         return new Option(this);
     }
	 
	}
	 
	 public Option(OptionBuilder builder){
			this._name=builder._name;
			this._startDate=builder._startDate;
			this._expireDate=builder._expireDate;
			this._period=builder._period;
			this._initialPrice=builder._initialPrice;
			this._strickPrice=builder._strickPrice;
			this._interestRate=builder._interestRate;
			this._volatility=builder._volatility;
			this._type=builder._type;
	 }

	public String getName() {
		return _name;
	}

	public int getStartDate() {
		return _startDate;
	}

	public int getExpireDate() {
		return _expireDate;
	}

	public int getPeriod() {
		return _period;
	}

	public double getInitialPrice() {
		return _initialPrice;
	}

	public double getStrickPrice() {
		return _strickPrice;
	}

	public double getInterestRate() {
		return _interestRate;
	}

	public double getVolatility() {
		return _volatility;
	}

	public String getType() {
		return _type;
	}
	
	/**
	 * Converts a Option object to a String representation
	 */
	
	@Override
	public String toString() {
		return String.format(
			"%s,%.2f,%.2f,%d,%.2f,%.4f,%s",
			_name,
			_initialPrice,
			_strickPrice,
			_period,
			_volatility,
			_interestRate,
			_type
		);
	}
	 

}
