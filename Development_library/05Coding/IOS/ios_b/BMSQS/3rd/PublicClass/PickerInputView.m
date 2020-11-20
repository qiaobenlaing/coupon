//
//  PickerInputTableViewCell.m
//  ShootStudio
//
//  Created by Tom Fewster on 18/10/2011.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "PickerInputView.h"

@implementation PickerInputView

@synthesize picker;


- (void)initalizeInputView {
    
	self.picker = [[UIPickerView alloc] initWithFrame:CGRectZero];
	self.picker.showsSelectionIndicator = YES;
	self.picker.autoresizingMask = UIViewAutoresizingFlexibleHeight;
	
	if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
        
		UIViewController *popoverContent = [[UIViewController alloc] init];
		popoverContent.view = self.picker;
        
        UIBarButtonItem *doneBarButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(done:)] ;
        
        popoverContent.navigationItem.rightBarButtonItem = doneBarButton;
        
        UINavigationController *myNavigationController = [[UINavigationController alloc] initWithRootViewController:popoverContent] ;
        
		popoverController = [[UIPopoverController alloc] initWithContentViewController:myNavigationController];

		popoverController.delegate = self;
        
	}
    
}


- (id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
		[self initalizeInputView];

        
        _backgroundImageView=[[UIImageView alloc]initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height)];
        //[self addSubview:_backgroundImageView];
        
        UIButton* dateButton=[UIButton buttonWithType:UIButtonTypeCustom];
        [dateButton setFrame:CGRectMake(0, 0, frame.size.width, frame.size.height)];   
        [dateButton addTarget:self action:@selector(didClickDateButton:) forControlEvents:UIControlEventTouchUpInside];
        //[self addSubview:dateButton];
        
        
        self.numberLabel=[[UILabel alloc]init];
        [_numberLabel setFont:[UIFont systemFontOfSize:20.0]];
        [_numberLabel setFrame:CGRectMake(7, 5, 100, 38)];
        [_numberLabel setBackgroundColor:[UIColor blueColor]];
        [_numberLabel setTextColor:[UIColor whiteColor]];
        //[self addSubview:_numberLabel];
        
        
        self.dateLabel=[[UILabel alloc]init];
        [_dateLabel setFont:[UIFont systemFontOfSize:12.0]];
        [_dateLabel setFrame:CGRectMake(90, 9, 200, 38)];
        [_dateLabel setBackgroundColor:[UIColor redColor]];
        [_dateLabel setTextColor:[UIColor whiteColor]];
        //[self addSubview:_dateLabel];
        
        _headLabel = [[UILabel alloc]init];
        //_headLabel.textColor = [UIColor colorWithRed:227.0/255.0 green:89.0/255.0 blue:63.0/255.0 alpha:1.0];
        [_headLabel setFont:[UIFont systemFontOfSize:18.0]];
    }
    return self;
}


- (id)initWithCoder:(NSCoder *)aDecoder {
    self = [super initWithCoder:aDecoder];
    if (self) {
		[self initalizeInputView];
    }
    return self;
}

- (UIView *)inputView {
	if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
		return nil;
	} else {
		return self.picker;
	}
}

- (UIView *)inputAccessoryView {
	if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
		return nil;
	} else {
		if (!inputAccessoryView) {
			inputAccessoryView = [[UIToolbar alloc] init];
			inputAccessoryView.barStyle = UIBarStyleDefault;
			inputAccessoryView.autoresizingMask = UIViewAutoresizingFlexibleHeight;
			[inputAccessoryView sizeToFit];
			CGRect frame = inputAccessoryView.frame;
			frame.size.height = 44.0f;
			inputAccessoryView.frame = frame;
            
			
            frame.origin.x = 0;
            _headLabel.frame = frame;// = [[UILabel alloc]initWithFrame:frame];
            //_headLabel.textColor = [UIColor colorWithRed:227.0/255.0 green:89.0/255.0 blue:63.0/255.0 alpha:1.0];
            //[_headLabel setFont:[UIFont systemFontOfSize:14.0]];
            //[inputAccessoryView addSubview:_headLabel];
            
            frame.size.height = 2;
            frame.size.width = 300;
            frame.origin.y = 42;
            frame.origin.x = 10;
            UIImageView *lineView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"cutoff_line"]];
            lineView.frame = frame;
            //[inputAccessoryView addSubview:lineView];
            
			UIBarButtonItem *doneBtn =[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(done:)];
			UIBarButtonItem *flexibleSpaceLeft = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
			
			NSArray *array = [NSArray arrayWithObjects:flexibleSpaceLeft, doneBtn, nil];
			[inputAccessoryView setItems:array];
           
		}
		return inputAccessoryView;
	}
}

- (void)done:(id)sender {
	[self resignFirstResponder];
    
    if ([picker isKindOfClass:[UIDatePicker class]]) {
        
        if (_delegate&& [_delegate respondsToSelector:@selector(picker:didClickDoneButton:)]) {
            [_delegate picker:self didClickDoneButton:sender];
        }
        
        return;
    }
    
    [popoverController dismissPopoverAnimated:YES];
    if (_delegate&& [_delegate respondsToSelector:@selector(picker:didClickDoneButton:)]) {
        [_delegate picker:self didClickDoneButton:sender];
    }
    
}

- (BOOL)becomeFirstResponder {

	
    [self.picker setNeedsLayout];
	
	return [super becomeFirstResponder];
}

- (BOOL)resignFirstResponder {

	return [super resignFirstResponder];
}


-(void)didClickDateButton:(id)sender{
    
    [self becomeFirstResponder];
}

#pragma mark -
#pragma mark Respond to touch and become first responder.

- (BOOL)canBecomeFirstResponder {
	return YES;
}

#pragma mark -
#pragma mark UIKeyInput Protocol Methods

- (BOOL)hasText {
	return YES;
}

- (void)insertText:(NSString *)theText {
}

- (void)deleteBackward {
}

#pragma mark -
#pragma mark UIPopoverControllerDelegate Protocol Methods

- (void)popoverControllerDidDismissPopover:(UIPopoverController *)popoverController {

	[self resignFirstResponder];
}

@end
